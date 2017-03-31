#coding=utf-8
#author: slam.li
#create: 2017/3/31

import zipfile
import shutil
import os
import sys

#版本号文件夹名
VERSION_NAME = '1.0.0'

VERSION_PATH = './release_apks/' + VERSION_NAME

#将编译apk文件夹备份到版本号对应的文件夹下
COPY_APK_PATH_DIR = VERSION_PATH + '/apk_0/'

#渠道列表文件
FLAVOR_FILE = 'all_channels.txt'

#zipalign路径
ZIPALIGN_PATH = '~/Library/Android/sdk/build-tools/25.0.2/zipalign'

#apk编译路径
BUILD_PATH = './app/build/outputs/app-release.apk'

#备份apk路径(系统生成的路径)
BAK_APK_PATH = './app/build/bakApk/'

#基带apk路径
APK_PATH = COPY_APK_PATH_DIR + 'app-release.apk'

#根据基带apk进行操作生成对应渠道包
CHANNEL_PATH = VERSION_PATH + '/channel/'

if __name__ == '__main__':
    if not os.path.exists(FLAVOR_FILE) or os.path.exists(VERSION_PATH):
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

    if len(sys.argv) == 2:
        lines = [sys.argv[1], ]

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
