#!/bin/sh

CONFIG=Debug
TARGET=iphoneos
CERTIFICATE=""

while getopts "c:r" opt; do
	case $opt in
		c)
			CERTIFICATE="${OPTARG}"
			;;
		r)
			CONFIG=Release
			;;
		t)
			TARGET="${OPTARG}"
			;;		
	esac
done

oldPwd=$(pwd)

cd $oldPwd/iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp -sdk "${TARGET}" -configuration "${CONFIG}" CODE_SIGN_IDENTITY="" CODE_SIGNING_REQUIRED=NO SYMROOT=$PWD/Build
cd Build/${CONFIG}-${TARGET}
mkdir -p Payload
cp -pr Violet.app Payload

if [ -n "${CERTIFICATE}" ]; then
	codesign --force --sign "${CERTIFICATE}" Payload/Violet.app/Violet
fi

zip -r $oldPwd/Violet.ipa Payload
cd $oldPwd
rm -rf $oldPwd/iosApp/Build
