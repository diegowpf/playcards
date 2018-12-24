FROM node:9.11.1-alpine
RUN yarn global add serve
COPY ./build/ /home/web/build/
EXPOSE 3000
WORKDIR /home/web/
ENTRYPOINT ["serve", "-l", "3000", "-s", "build"]
