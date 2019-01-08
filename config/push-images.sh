eval "$(aws ecr get-login --no-include-email --region us-east-1)"
DOCKER_REPO_URL=068681799287.dkr.ecr.us-east-1.amazonaws.com

docker tag bytecubedlabs/playcards-client ${DOCKER_REPO_URL}/playcards-client:${CIRCLE_BUILD_NUM}
docker tag bytecubedlabs/playcards-server ${DOCKER_REPO_URL}/playcards-server:${CIRCLE_BUILD_NUM}
docker tag bytecubedlabs/playcards-nlp ${DOCKER_REPO_URL}/playcards-nlp:${CIRCLE_BUILD_NUM}
docker tag bytecubedlabs/playcards-teams ${DOCKER_REPO_URL}/playcards-teams:${CIRCLE_BUILD_NUM}

docker push ${DOCKER_REPO_URL}/playcards-client:${CIRCLE_BUILD_NUM}
docker push ${DOCKER_REPO_URL}/playcards-server:${CIRCLE_BUILD_NUM}
docker push ${DOCKER_REPO_URL}/playcards-nlp:${CIRCLE_BUILD_NUM}
docker push ${DOCKER_REPO_URL}/playcards-teams:${CIRCLE_BUILD_NUM}
