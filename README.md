# makephrase

A passphrase generator

## Installation

First, install [Leiningen](http://leiningen.org/) if you don't have it already.

    $ git clone https://github.com/emdeesee/makephrase.git
    $ cd makephrase
    $ lein bin
    $ cp target/makephrase ~/bin

## Usage

    $ makephrase [options]

## Options

  -n, --how-many <number to generate>  default: 1
  -l, --length <number of words>       default: 4
  -h, --help


## Examples

    $ makephrase
    Choosier pr4m rams predator
    $ makephrase -l 2
    bru5hing smiLing
    $ makephrase -l 2 -n 2
    eyeS!ght viceroy
    c4gey Pouts

### Todo

 + Write tests for things (i know, i know...)
 + Specify alternative word source on command line
 + A flag to enable or disable leetification and add-cap
 + Specify number of bits of entropy, as alternative to number of words
 + Flag to generate phrases in ALL CAPS

## License

Copyright © 2015 Michael Cornelius

Distributed under the Eclipse Public License either version 1.0.

