# GoogleApi
Google storage upload sample project

<<How to test google drive api>>
1. Google 에 로그인
2. https://console.developers.google.com/apis 접속
3. 사용자 인증 정보 > 사용자 인증 정보 만들기 > OAuth 2.0 클라이언트 ID
4. 애플리케이션 유형: 데스크톱 앱
5. 아래 내용을 복사하여 /GoogleApi/src/main/java/com/clt/google/drive/credentials.json 생성 후 클라이언트 ID/클라이언트 보안 비밀을 발급받은 정보로 수정

{
	"installed": {
		"client_id": "클라이언트 ID",
		"project_id": "quickstart-1610085309671",
		"auth_uri": "https://accounts.google.com/o/oauth2/auth",
		"token_uri": "https://oauth2.googleapis.com/token",
		"auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
		"client_secret": "클라이언트 보안 비밀",
		"redirect_uris": [
			"urn:ietf:wg:oauth:2.0:oob",
			"http://localhost"
		]
	}
}

6. /GoogleApi/src/test/java/com/clt/google/DriveAppTest.java 으로 JUnit 실행
7. 인증 브라우저 창이 리다이렉트 되면 구글 계정으로 드라이브 접근허용
8. 이클립스 콘솔에 구글드라이브 파일목록 출력확인

** 한 번만 인증하게 되면 tokens 폴더가 프로젝트에 만들어지고 이 후 부터는 인증없이 API 사용 가능
