docker tag bytecubedlabs/playcards-nlp:latest bytecubedlabs/playcards-nlp:$CIRCLE_BUILD_NUM
docker tag bytecubedlabs/playcards-client:latest bytecubedlabs/playcards-client:$CIRCLE_BUILD_NUM
docker tag bytecubedlabs/playcards-server:latest bytecubedlabs/playcards-server:$CIRCLE_BUILD_NUM
docker tag bytecubedlabs/playcards-teams:latest bytecubedlabs/playcards-teams:$CIRCLE_BUILD_NUM
docker push bytecubedlabs/playcards-teams:$CIRCLE_BUILD_NUM
docker push bytecubedlabs/playcards-server:$CIRCLE_BUILD_NUM
docker push bytecubedlabs/playcards-client:$CIRCLE_BUILD_NUM
docker push bytecubedlabs/playcards-nlp:$CIRCLE_BUILD_NUM
