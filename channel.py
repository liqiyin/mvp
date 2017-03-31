#coding=utf-8
import zipfile
import shutil
import os
import sys

FLAVOR_FILE = 'all_channels.txt'

#zipalign路径
ZIPALIGN_PATH = '~/Library/Android/sdk/build-tools/25.0.2/zipalign'

#apk编译路径
BUILD_PATH = './app/build/outputs/apk/'

#版本号文件夹
VERSION_NAME = '1_0_0/'

#将编译apk文件夹备份到版本号对应的文件夹下
COPY_APK_PATH_DIR = './release_apks/' + VERSION_NAME + 'apk/'

#基带apk路径
APK_PATH = COPY_APK_PATH_DIR + 'app-release.apk'

#根据基带apk进行操作生成对应渠道包
CHANNEL_PATH = COPY_APK_PATH_DIR + 'channel/'

if __name__ == '__main__':
    if not os.path.exists(FLAVOR_FILE):
        pass

    hasApk = os.path.exists(BUILD_PATH + 'app-release.apk')
    if len(sys.argv) == 1 or not hasApk:
        os.system('./gradlew assembleRelease')

    if not os.path.exists(COPY_APK_PATH_DIR):
        os.makedirs(COPY_APK_PATH_DIR)

    #将编译好的apk复制到当前文件夹
    for f in os.listdir(BUILD_PATH):
        shutil.copy(BUILD_PATH + f, COPY_APK_PATH_DIR + f)

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
