# pylint: disable=missing-docstring, invalid-name, line-too-long, redefined-outer-name, too-many-arguments

from setuptools import setup

setup(
    name='amazonia',
    version='0.1.0',
    description="GA AWS CloudFormation creation library",
    author="Lazar Bodor",
    author_email="lazar.bodor@ga.gov.au",
    url="https://github.com/GeoscienceAustralia/amazonia",
    license="New BSD license",
    packages=['amazonia'],
    install_requires=[
        'troposphere'
    ],
    scripts=['scripts/viz'],
)
