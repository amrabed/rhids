# RHIDS
[![Build Status](https://travis-ci.org/amrabed/rhids.svg)](https://travis-ci.org/amrabed/rhids)
[![Known Vulnerabilities](https://snyk.io/test/github/amrabed/rhids/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/amrabed/rhids?targetFile=build.gradle)
[![GitHub issues](https://img.shields.io/github/issues/amrabed/rhids.svg)](https://github.com/amrabed/rhids/issues)
[![GitHub (pre-)release](https://img.shields.io/github/release/amrabed/rhids/all.svg)](https://github.com/amrabed/rhids/releases)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Host-based Intrusion Detection System for Linux Containers<a href="#footnote" id="ref"><sup>*</sup></a>

To use `rhids`, please make sure you have [`strace-docker`](https://github.com/amrabed/strace-docker) installed. `rhids` depends on [`strace-docker`](https://github.com/amrabed/strace-docker) for collecting system calls from running containers.

## Usage
### Basic install
    git clone https://github.com/amrabed/rhids && sudo ./rhids/install
    sudo su 
    rhids -h

### Using Docker
    docker run -it --rm --name rhids -v /var/log/strace-docker:/var/log/strace-docker amrabed/rhids
    
    
<a id="footnote" href="#ref"><sup>*</sup></a> Implemented as part of my Ph.D. dissertation research. See [this paper](https://arxiv.org/abs/1611.03056) for more details
