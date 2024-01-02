FROM node:20.9.0-bookworm
RUN usermod --home /home/bouzuya --login bouzuya node && \
  mkdir -p /home/bouzuya/bbna
WORKDIR /home/bouzuya/bbna
RUN npm install --global eas-cli fixpack
