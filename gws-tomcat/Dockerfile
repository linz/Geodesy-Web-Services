FROM lnl7/nix:1.11.4

RUN nix-instantiate --eval -E 'fetchTarball https://github.com/NixOS/nixpkgs/archive/354fd37.tar.gz' \
 && ln -s /nix/store/zygr4nwg01dzs9pj1mx3z2qbfaw15zb5-354fd37.tar.gz /root/.nix-defexpr/nixpkgs

RUN nix-env -iA \
  nixpkgs.coreutils \
  nixpkgs.unzip \
  nixpkgs.gnused \
  nixpkgs.which \
  nixpkgs.gnugrep \
  nixpkgs.rsync \
  nixpkgs.curl \
  nixpkgs.jre8 \
  nixpkgs.tomcat8 

ADD env.sh /
ADD entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
# CMD ["catalina.sh", "run"]
