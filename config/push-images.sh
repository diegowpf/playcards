eval "$(aws ecr get-login --no-include-email --region us-east-1)"
DOCKER_REPO_URL=126555851281.dkr.ecr.us-east-1.amazonaws.com

docker tag bytecubedlabs/playcards-client ${DOCKER_REPO_URL}/playcards-client:${CIRCLE_SHA1}
docker tag bytecubedlabs/playcards-server ${DOCKER_REPO_URL}/playcards-server:${CIRCLE_SHA1}
docker tag bytecubedlabs/playcards-nlp ${DOCKER_REPO_URL}/playcards-nlp:${CIRCLE_SHA1}
docker tag bytecubedlabs/playcards-teams ${DOCKER_REPO_URL}/playcards-teams:${CIRCLE_SHA1}

docker push ${DOCKER_REPO_URL}/playcards-client:${CIRCLE_SHA1}
docker push ${DOCKER_REPO_URL}/playcards-server:${CIRCLE_SHA1}
docker push ${DOCKER_REPO_URL}/playcards-nlp:${CIRCLE_SHA1}
docker push ${DOCKER_REPO_URL}/playcards-teams:${CIRCLE_SHA1}
