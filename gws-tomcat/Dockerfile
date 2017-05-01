FROM lnl7/nix:1.11.4

RUN nix-instantiate --eval -E 'fetchTarball https://github.com/NixOS/nixpkgs-channels/archive/f0fac3b.tar.gz'
RUN rm /root/.nix-defexpr/nixpkgs
RUN ln -s /nix/store/jf0qvw9sns1yjmdsfsl629j0yqdrc5vi-f0fac3b.tar.gz /root/.nix-defexpr/nixpkgs

RUN nix-env -iA  nixpkgs.coreutils
RUN nix-env -iA  nixpkgs.unzip
RUN nix-env -iA  nixpkgs.gnused
RUN nix-env -iA  nixpkgs.which
RUN nix-env -iA  nixpkgs.gnugrep
RUN nix-env -iA  nixpkgs.rsync
RUN nix-env -iA  nixpkgs.curl
RUN nix-env -iA  nixpkgs.jre8
RUN nix-env -iA  nixpkgs.tomcat8 
RUN nix-env -iA  nixpkgs.vim

ADD env.sh /
ADD entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
# CMD ["catalina.sh", "run"]
