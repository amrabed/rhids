# RHIDS
[![Build Status](https://travis-ci.org/amrabed/rhids.svg)](https://travis-ci.org/amrabed/rhids)

Host-based Intrusion Detection System for Linux Containers<a href="#footnote" id="ref"><sup>*</sup></a>

To use `rhids`, please make sure you have [`strace-docker`](https://github.com/amrabed/strace-docker) installed. `rhids` depends on [`strace-docker`](https://github.com/amrabed/strace-docker) for collecting system calls from running containers.

## Usage
### Basic install
    git clone https://github.com/amrabed/rhids && sudo ./rhids/install
    sudo rhids -h

### Using Docker
    docker run -it --rm --name rhids -v /var/log/strace-docker:/var/log/strace-docker amrabed/rhids

### Using Python (experimental)
    wget https://raw.githubusercontent.com/amrabed/rhids/experimental/rhids.py
    python3 rhids.py <input-file> <epoch-size> <detection-threshold>
    
<a id="footnote" href="#ref"><sup>*</sup></a> Implemented as part of my Ph.D. dissertation research. See [this paper](https://arxiv.org/abs/1611.03056) for more details
