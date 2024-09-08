#!/bin/sh

oldPwd=$(pwd)

cd $oldPwd/iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp -sdk iphoneos -configuration Release CODE_SIGN_IDENTITY="" CODE_SIGNING_REQUIRED=NO SYMROOT=$PWD/Build
cd Build/Release-iphoneos
mkdir -p Payload
cp -pr Violet.app Payload
zip -r $oldPwd/Violet.ipa Payload
cd $oldPwd
rm -rf $oldPwd/iosApp/Build
