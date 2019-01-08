docker tag bytecubedlabs/playcards-nlp:latest bytecubedlabs/playcards-nlp:$CIRCLE_SHA1
docker tag bytecubedlabs/playcards-client:latest bytecubedlabs/playcards-client:$CIRCLE_SHA1
docker tag bytecubedlabs/playcards-server:latest bytecubedlabs/playcards-server:$CIRCLE_SHA1
docker tag bytecubedlabs/playcards-teams:latest bytecubedlabs/playcards-teams:$CIRCLE_SHA1
docker push bytecubedlabs/playcards-teams:$CIRCLE_SHA1
docker push bytecubedlabs/playcards-server:$CIRCLE_SHA1
docker push bytecubedlabs/playcards-client:$CIRCLE_SHA1
docker push bytecubedlabs/playcards-nlp:$CIRCLE_SHA1
