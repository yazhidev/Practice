#!/bin/bash

read -p "构建类型：1 测试（debug），2 预发（pre），3 正式（release），4 多渠道包， 5 单渠道包 。请选择（输入数字）" type

branchName=`git rev-parse --abbrev-ref HEAD`
releaseApkPath=./app/release_file/app-release.apk
channelPath=./app/channel
channelApkOutputPath=./app/build/outputs/channels/

if [ "$type" = "1" ]; then
	echo "已选择构建类型：测试"
	# 获取当前分支名
	if [[ $branchName == *"pre"* ]] || [[ $branchName == *"release"* ]] ;then
		echo "当前分支 $branchName ，该分支不允许构建测试。请遵循规范，在版本对应的 dev 分支修改并测试"
		exit
	fi

	gradle assembleDebug

elif [ "$type" = "2" ]; then
	echo "已选择构建类型：预发"
	# 获取当前分支名
	if [[ $branchName != *"pre"* ]];then
		echo "当前分支 $branchName ，该分支不允许构建预发包。请遵循规范，在版本对应的 pre 分支打预发包"
		exit
	fi

	gradle assemblePre

elif [ "$type" = "3" ]; then
	echo "已选择构建类型：正式"
	# 获取当前分支名
	if [[ $branchName != *"release"* ]];then
		echo "当前分支 $branchName ，该分支不允许构建正式包。请遵循规范，在版本对应的 release 分支打预发包"
		exit
	fi

	gradle assembleRelease

elif [ "$type" = "4" ]; then
	echo "已选择构建类型：多渠道包。渠道配置文件：./app/channel。渠道包输出路径：$channelApkOutputPath"

	if [ ! -e "$releaseApkPath" ];then
        echo "请确认安装包 ${releaseApkPath} 是否存在"
        exit
    fi

    if [ ! -e "$channelPath" ];then
        echo "请确认渠道配置文件 ${channelPath} 是否存在"
        exit
    fi

	java -jar walle-cli-all.jar batch -f ${channelPath} ${releaseApkPath} ${channelApkOutputPath}

	#将未写入渠道信息的包作为 360 渠道的包复制到输出路径
	cp ${releaseApkPath} ./app/release_file/app-release-360.apk
    mv ./app/release_file/app-release-360.apk ./app/build/outputs/channels/app-release-360.apk

elif [ "$type" = "5" ]; then
	read -p "已选择构建类型：单渠道包。请输入渠道信息 ：" channel
    java -jar walle-cli-all.jar put -c ${channel} ${releaseApkPath} "${channelApkOutputPath}/app-release-${channel}.apk"
    echo "渠道包输出路径：${channelApkOutputPath}/app-release-${channel}.apk"

else
    echo "错误：未知类型"
    exit 0
fi


