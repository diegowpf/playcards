curl -X DELETE http://server.platform.bytecubedlabs.co/playcards

curl \
  -X POST \
  -F "file=@./initialize/CIN-32.pptx" \
  http://server.platform.bytecubedlabs.co/playcards/team/import/c679919f-d524-3f75-ad2a-5161706e12a5

curl \
  -X POST \
  -F "file=@./initialize/BLT-44.pptx" \
  http://server.platform.bytecubedlabs.co/playcards/team/import/c679919f-d524-3f75-ad2a-5161706e12a5

curl \
  -X POST \
  -F "file=@./initialize/WAS-42.pptx" \
  http://server.platform.bytecubedlabs.co/playcards/team/import/c679919f-d524-3f75-ad2a-5161706e12a5


curl \
  -X POST \
  -F "file=@./initialize/DOL-21.pptx" \
  http://server.platform.bytecubedlabs.co/playcards/team/import/c679919f-d524-3f75-ad2a-5161706e12a5

