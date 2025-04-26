#!/bin/bash

# 첫 번째 인자 받기
VERSION=$1

# 인자가 비었는지 확인
if [ -z "$VERSION" ]; then
  echo "에러: 버전 인자를 입력해야 합니다."
  echo "사용법: ./build_push.sh <버전>"
  exit 1
fi

# Docker Build
docker build -t frontend:$VERSION .

# Docker Tag (latest로 재태그)
docker tag frontend:$VERSION iiilee0907/frontend:latest

# Docker Push
docker push iiilee0907/frontend:latest

echo "✅ Docker build/tag/push 완료: frontend:$VERSION -> iiilee0907/frontend:latest"

