FROM lnl7/nix:1.11.4

RUN nix-instantiate --eval -E 'fetchTarball https://github.com/NixOS/nixpkgs/archive/354fd37.tar.gz' \
 && ln -s /nix/store/zygr4nwg01dzs9pj1mx3z2qbfaw15zb5-354fd37.tar.gz /root/.nix-defexpr/nixpkgs

RUN nix-env -iA \
  nixpkgs.busybox \
  nixpkgs.jre8 \
  nixpkgs.rsync \
  nixpkgs.tomcat8

ADD entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
# CMD ["catalina.sh", "run"]
