FROM rust:1.77.0-bookworm

RUN useradd --create-home bouzuya \
  && mkdir -p /home/bouzuya/bbna
RUN apt-get update && apt-get install -y \
  jq \
  mold \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*
RUN rustup component add \
  clippy \
  rustfmt
RUN cargo install \
  cargo-watch
WORKDIR /home/bouzuya/bbna
