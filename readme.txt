[컴파일부터 실행까지]
  step1 (컴파일)
    "src" 폴더로 이동, 아래 커맨드 입력
      javac -d ..\bin\ snakegame\rmisslsocketfactory\*.java snakegame\element\*.java snakegame\server\*.java snakegame\client\*.java -encoding UTF-8
  step2 (rmiregistry 실행)
    "bin" 폴더로 이동, 아래 커맨드 입력
      start rmiregistry
  step3 (서버 실행)
    "bin" 폴더에서 아래 커맨드 입력
      java snakegame.server.ServerDo
  step4 (클라이언트 실행)
    "bin" 폴더에서 아래 커맨드 입력
      java snakegame.client.ClientDo

option 1
[Key Store 생성]
  step1 (Server Key Store 및 Key 생성)
    "bin" 폴더로 이동, 아래 커맨드 입력 (비번: 123456, 다른 비번으로 수정시 RMISSLServerSocketFactory.java 코드를 해당 비번으로 변경)
      keytool -genkey -keystore .\serverkeystore -keyalg RSA -storetype JKS
  step2 (serverkeystore에서 인증서 export하기)
    "bin" 폴더에서 아래 커맨드 입력 (비번은 Server Key Store 생성 당시 사용했던 비번)
      keytool -export -keystore serverkeystore -file certification.cer
  step3 (Client Key Store 생성 및 인증서 import하기)
    "bin" 폴더에서 아래 커맨드 입력 (비번: 123456, 다른 비번으로 수정시 StartWindow.java 코드를 해당 비번으로 변경)
      keytool -import -keystore .\clientkeystore -file certification.cer

option 2
[jar 파일 만들기 (실행파일)]
  step1 (server.jar 만들기)
    "bin" 폴더로 이동, 아래 커맨드 입력
      jar -cvfm server.jar ..\META-INF\server_manifest.mf .\snakegame\server .\snakegame\element .\snakegame\rmisslsocketfactory .\serverkeystore
  step2 (client.jar 만들기)
    "bin" 폴더에서 아래 커맨드 입력
      jar -cvfm client.jar ..\META-INF\client_manifest.mf .\snakegame\client .\snakegame\element .\snakegame\rmisslsocketfactory .\clientkeystore

option 3
[java 환경변수 설정]
  JAVA_HOME
    F:\Java\jdk1.8.0_211
  Path
    %JAVA_HOME%\bin
  CLASSPATH
    %JAVA_HOME%\lib;.

