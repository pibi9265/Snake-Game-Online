// ssl keystore 및 key 생성 (이미 만들어 놓음, 비번 123456)
// bin폴더에서
keytool -genkey -keystore .\serverstore -keyalg RSA -storetype JKS

// keystore에서 인증서 export하기 (이미 만들어 놓음, 비번 123456)
// bin폴더에서
keytool -export -keystore serverstore -file certification.cer

// client에서 keystore 생성하고 keystore애 인증서 import하기 (이미 만들어 놓음, 비번 123456)
// bin폴더에서
keytool -import -keystore .\clientstore -file certification.cer

// 컴파일
// src폴더에서
javac -d ..\bin\ snakegame\rmisslsocketfactory\*.java snakegame\element\*.java snakegame\server\*.java snakegame\client\*.java -encoding UTF-8

// rmiregistry 실행하기
// bin폴더에서
start rmiregistry

// 실행
// bin폴더에서
java snakegame.server.ServerDo
java snakegame.client.ClientDo

// jar 파일 만들기 (실행파일)
// bin폴더에서
jar -cvfm server.jar ..\META-INF\server_manifest.mf .\snakegame\server .\snakegame\element .\snakegame\rmisslsocketfactory
jar -cvfm client.jar ..\META-INF\client_manifest.mf .\snakegame\client .\snakegame\element .\snakegame\rmisslsocketfactory

// java 환경변수
////////////////////////////////////////
JAVA_HOME
F:\Java\jdk1.8.0_211

Path
%JAVA_HOME%\bin

CLASSPATH
%JAVA_HOME%\lib;.
////////////////////////////////////////
