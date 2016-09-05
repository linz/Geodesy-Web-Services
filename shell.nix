let
  pkgs = import <nixpkgs> {};
  env = with pkgs; buildEnv {
    name = "geodesyDevEnv";
    paths = [
      awscli
      python
      python3
      pythonPackages.credstash
    ];
  };
in
  pkgs.runCommand "dummy" {
    buildInputs = [
      env
    ];
  } ""





