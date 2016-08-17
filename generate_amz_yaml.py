#!/usr/bin/env python3
import argparse
import sys
import os
import yaml
import logging
from jinja2 import Environment, FileSystemLoader

PATH = os.path.dirname(os.path.abspath(__file__))
TEMPLATE_ENVIRONMENT = Environment(
    autoescape=False,
    loader=FileSystemLoader(PATH),
    extensions=['jinja2.ext.autoescape'],
    trim_blocks=False,
    keep_trailing_newline=True)


def get_args(argv):
    """
    Handles all the arguments that are passed into the script
    :return: Returns a parsed version of the arguments.
    """
    parser = argparse.ArgumentParser(
        description='Script to substitute variables and generate yaml')
    parser.add_argument("-o", "--output",
                        help="Path to output file",
                        dest="output_file",
                        default='outputs.yaml'),
    parser.add_argument("-b", "--base",
                        help="Path to base file with jinja placeholders",
                        dest="base_file",
                        default='base.yaml'),
    parser.add_argument("-v", "--variables",
                        help="Path to variable file with jinja substitutions",
                        dest="variables_file",
                        default='variables.yaml')
    return parser.parse_args(argv)


def render_template(template_filename, context):
    return TEMPLATE_ENVIRONMENT.get_template(template_filename).render(context)


def load_yaml(name):
    """
    Load yaml variables
    :param name: name to retrieve yaml variables from
    """
    with open(os.path.join(PATH, name), 'r') as input_yaml:
        return yaml.safe_load(input_yaml)


def generate_yaml(output_file, base_file, variables_file):
    """
    Loads variables from variables.yaml
    Loads userdata from all files in userdata/ directory
    """
    application_yaml = load_yaml(variables_file)

    # for filename in os.listdir('userdata'):
    #     filepath = 'userdata/' + filename
    #     name = os.path.splitext(filename)[0]
    #     with open(filepath, 'r') as input_file:
    #         application_yaml[name] = input_file.read()

    out_yaml = render_template(base_file, application_yaml)
    with open(output_file, 'w') as f:
        f.write(out_yaml)


def main(argv):
    """
    Performs variable substitution on a base template and generates a yaml
    file from the result
    :param argv:
    """
    # Logging
    logging.basicConfig(
        level=logging.INFO
    )

    args = get_args(argv)
    logging.info('output_file = {0}'.format(args.output_file))
    logging.info('base_file = {0}'.format(args.base_file))
    logging.info('variables_file = {0}'.format(args.variables_file))
    generate_yaml(args.output_file, args.base_file, args.variables_file)

if __name__ == "__main__":
    main(sys.argv[1:])
