FROM lnl7/nix:1.11.4

RUN nix-instantiate --eval -E 'fetchTarball https://github.com/NixOS/nixpkgs/archive/354fd37.tar.gz' \
 && ln -s /nix/store/zygr4nwg01dzs9pj1mx3z2qbfaw15zb5-354fd37.tar.gz /root/.nix-defexpr/nixpkgs

RUN nix-env -iA \
  nixpkgs.busybox \
  nixpkgs.jre8 \
  nixpkgs.rsync \
  nixpkgs.tomcat8

ADD target/geodesy-web-services.war /webapps/ROOT.war

RUN (cd /webapps && mkdir ROOT && unzip ROOT.war -d ROOT)
RUN sed -i 's,${geodesy-db-url},jdbc:postgresql://db/geodesydb,' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-username}/geodesy/' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-password}/geodesypw/' /webapps/ROOT/META-INF/context.xml

ADD src/main/docker/webserver/entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
CMD ["catalina.sh", "run"]
