let
  pkgs = import <nixpkgs> {};
  env = with pkgs; buildEnv {
    name = "geodesyDevEnv";
    paths = [
      awscli
      python
      python3
      pythonPackages.credstash
      packer
    ];
  };
in
  pkgs.runCommand "dummy" {
    buildInputs = [
      env
    ];
  } ""





