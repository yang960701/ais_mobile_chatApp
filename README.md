# AIS_mobile_chatApp

## This is the AIS education training team development repository.

### RULE
 - Main과 Dev 브런치에는 직접적으로 푸쉬하지 않기(작업시 항상 Dev로부터 `feature/{Issues.No}-{작업내용}` 형식의 브런치 따서 작업.
 - Dev에서 따온 브런치는 한가지 기능 단위로 만들기.(ex 로그인브런치1, 회원가입브런치1)
 - Dev로 PullRequest 하기전 불필요 한 코드가 있을지 모르니 다른 1명에게 코드리뷰를 받은 후 PullRequest작업하기.
 
### COMMAND
 - 처음 시작 `git clone https://github.com/AisMobileTeam/ais_mobile_chatApp.git`  클론 받기.
 - 브런치만들기 `git branch {브런치 이름}` , `git checkout -b {브런치이름}`2번째 명령어는 브런치를 만들며 바로이동.
 - 브런치 이동 `git checkout {브런치 이름}`
 - 현재 브런치 확인 `git branch` 전체 브런치 확인시 `-a` 추가
 - 저장 `git add {작업파일}` 전채 일괄 저장 시 `git add .`
 - 커밋 `git commit -m "{작업내용}"` 작업내용 확정.
 - 푸시 `git push` 작업내용  리포지토리로 보내기.
 - 풀 `git pull` 깃헙 리포지토리에 저장된 최신 데이터를 가져오기. (병합)
 - 펫치 `git fetch` 깃헙리포지토리에서 최신데이터 가져오기. (코드만 가져와 비교가능)
 
 
### Reference
`https://velog.io/@kim-jaemin420/Git-branch-naming`
