#coding=utf-8
#author: slam.li
#create: 2017/3/31

import zipfile
import shutil
import os
import sys

#版本号文件夹存放路径
VERSION_PATH = ''

#将编译apk文件夹备份到版本号对应的文件夹下
COPY_APK_PATH_DIR = ''

#基带apk路径
APK_PATH = ''

#根据基带apk进行操作生成对应渠道包
CHANNEL_PATH = ''

#渠道列表文件
FLAVOR_FILE = 'all_channels.txt'

#zipalign路径
ZIPALIGN_PATH = '~/Library/Android/sdk/build-tools/25.0.2/zipalign'

#apk编译路径
BUILD_PATH = './app/build/outputs/app-release.apk'

#备份apk路径(系统生成的路径)
BAK_APK_PATH = './app/build/bakApk/'

def initArgs(versionName) :
    global VERSION_PATH
    global COPY_APK_PATH_DIR
    global APK_PATH
    global CHANNEL_PATH

    VERSION_PATH = './release_apks/' + versionName
    COPY_APK_PATH_DIR = VERSION_PATH + '/apk_0/'
    APK_PATH = COPY_APK_PATH_DIR + 'app-release.apk'
    CHANNEL_PATH = VERSION_PATH + '/channel/'

if __name__ == '__main__':
    #不执行编译工作的条件:
    #channel配置文件不存在
    #未加版本号参数
    if not os.path.exists(FLAVOR_FILE):
        print('渠道配置文件不存在 请配置channel.txt文件')
        sys.exit()

    if len(sys.argv) != 2:
        print('未指定版本号')
        sys.exit()

    #参数初始化
    initArgs(sys.argv[1])

    #若该版本号文件夹已经存在
    if os.path.exists(VERSION_PATH):
        print('版本号文件夹已存在 请手动删除')
        sys.exit()

    #目标文件夹已存在
    if os.path.exists(COPY_APK_PATH_DIR):
        print('源apk文件已存在 请手动删除源apk文件')
        sys.exit()

    #先删除BAK_APK_PATH下的所有文件
    if os.path.exists(BAK_APK_PATH):
        shutil.rmtree(BAK_APK_PATH)

    #编译apk
    os.system('./gradlew assembleRelease')

    if os.path.exists(COPY_APK_PATH_DIR):
        shutil.rmtree(COPY_APK_PATH_DIR)

    #将编译好的apk复制到release_apks/apk/中
    for f in os.listdir(BAK_APK_PATH):
        shutil.copytree(BAK_APK_PATH + f, COPY_APK_PATH_DIR)

    #删除bak文件夹中的文件
    shutil.rmtree(BAK_APK_PATH)

    emptyFile = 'xxx.txt'
    f = open(emptyFile, 'w')
    f.close()
    with open(FLAVOR_FILE, 'r') as f:
        contents = f.read()
    lines = contents.split('\n')

    if not os.path.exists(CHANNEL_PATH):
        os.mkdir(CHANNEL_PATH)
    else:
        for f in os.listdir(CHANNEL_PATH):
            os.remove(CHANNEL_PATH + f)

    for line in lines:
        print line
        channel = 'channel_' + line
        destfile = CHANNEL_PATH + '%s.apk' % channel
        shutil.copy(APK_PATH, destfile)
        zipped = zipfile.ZipFile(destfile, 'a')
        channelFile = "META-INF/{channelname}".format(channelname=channel)
        zipped.write(emptyFile, channelFile)
        zipped.close()
    os.remove('./xxx.txt')

    for f in os.listdir(CHANNEL_PATH):
        if f.endswith('.apk'):
            os.system(ZIPALIGN_PATH +' -f -v 4 ' + CHANNEL_PATH + f + ' ' + CHANNEL_PATH + 'temp-' + f)
            os.remove(CHANNEL_PATH + f)

    for f in os.listdir(CHANNEL_PATH):
        if f.startswith('temp-'):
            os.system(ZIPALIGN_PATH +' -f -v 4 ' + CHANNEL_PATH + f + ' ' + CHANNEL_PATH + f.replace('temp-', ''))
            os.remove(CHANNEL_PATH + f)
