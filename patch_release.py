#coding=utf-8
#author: slam.li
#create: 2017/3/31

import zipfile
import shutil
import os
import sys

#版本号文件夹名(打补丁时必须修改 当前app为当前app版本号)
VERSION_NAME = '1.0.0'

#补丁版本号(若现在只有apk_0文件夹 那么PATCH_VERSION则+1 必须修改)
PATCH_VERSION = 1

#源APK路径
BASE_BAK_APK = './release_apks/' + VERSION_NAME + '/apk_' + str(PATCH_VERSION - 1) + '/'

#打过补丁之后的新APK路径
NEW_BAK_APK = './release_apks/' + VERSION_NAME + '/apk_' + str(PATCH_VERSION) + '/'

#系统备份apk路径
SYSTEM_BAK_APK = './app/build/bakApk/'

#系统读取的积累apk路径
SYSTEM_LOAD_BASE_APK = SYSTEM_BAK_APK + 'tempApk/'

#系统生成的apk补丁路径
SYSTEM_APK_PATCH_PATH = './app/build/outputs/patch/release/patch_signed_7zip.apk'

if __name__ == '__main__':
    if not os.path.exists(BASE_BAK_APK):
        sys.exit()

    #删除系统备份文件夹中文件
    if os.path.exists(SYSTEM_BAK_APK):
        shutil.rmtree(SYSTEM_BAK_APK)

    #将基带文件夹复制到系统备份文件夹中作为基带apk
    shutil.copytree(BASE_BAK_APK, SYSTEM_LOAD_BASE_APK)

    #执行补丁命令
    os.system('./gradlew buildTinkerPatchRelease')

    #删除之前作为基带apk的文件夹
    shutil.rmtree(SYSTEM_LOAD_BASE_APK)

    #将打过补丁之后的apk文件备份
    for f in os.listdir(SYSTEM_BAK_APK):
        shutil.copytree(SYSTEM_BAK_APK + f, NEW_BAK_APK)

    #复制完成将源文件删除
    shutil.rmtree(SYSTEM_BAK_APK)

    #将生成的补丁文件进行备份
    shutil.copy(SYSTEM_APK_PATCH_PATH, NEW_BAK_APK + 'patch.apk')
