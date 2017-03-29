import re as regex
import collections
import queue
import sys

class rates:
    def __init__(self):
        self.true_positives = 0
        self.false_positives = 0
        self.true_negatives = 0
        self.false_negatives = 0

    def increment_fp(self):
        self.false_positives += 1

    def increment_tp(self):
        self.true_positives += 1

    def increment_fn(self):
        self.false_negatives += 1

    def increment_tn(self):
        self.true_negatives += 1

    def print(self):
        print('False positives: %d' % self.false_positives)
        print('True positives: %d' % self.true_positives)
        print('False negatives: %d' % self.false_negatives)
        print('True positives: %d' % self.true_negatives)


class rhids:
    def __init__(self, file, epoch_size = 1000, detection_threshold = 10):
        self.reader = open(file, 'r')
        self.epoch_size = int(epoch_size)
        self.detection_threshold = int(detection_threshold)
        self.isTraining = True
        self.isUnderAttack = False
        self.index_map = self._build_index_map()
#        print(self.index_map)
        self.window = queue.Queue(10)
        self.bosc = [0] * (len(self.index_map) + 1)
        self.db = set()
        self.mismatches = 0
        self.syscalls = 0
        self.rates = rates()
        self.training_epochs = 0
        self.total_epochs = 0
        self.is_already_declared = False
        self.parse()

    @staticmethod
    def _read_syscalls():
        return [x.replace('\n', '') for x in open("syscalls").readlines()]

    def _build_index_map(self):
        syscalls = self._read_syscalls()
        count_pairs = sorted(collections.Counter(syscalls).items(), key=lambda x: (-x[1], x[0]))
        words, _ = list(zip(*count_pairs))
        return dict(zip(words, range(len(words))))

    def _get_syscall_index(self, syscall):
        return self.index_map[syscall] if syscall in self.index_map else len(self.index_map)

    def add_bosc(self, bosc):
        self.db.add(str(bosc))

    def is_normal_bosc(self, bosc):
        return str(bosc) in self.db

    def parse(self):
        for line in self.reader:
            if regex.search("STOP TRAINING", line):
                self.isTraining = False
                self.dump_db_to_file()
                print("Training complete")
            elif regex.search("START ATTACK", line):
                print("Attack Start")
                self.isUnderAttack = True
            elif regex.search("END ATTACK", line):
                print("Attack End")
                self.isUnderAttack = False
            else:
                match = regex.search(r"([a-z_]+[0-9]*)", line)
                if match:
                    self.slide_window(match.group(1))

    def slide_window(self, syscall):
        if self.window.full():
            out = self.window.get()
            self.bosc[self._get_syscall_index(out)] -= 1
        self.window.put(syscall)
        index = self._get_syscall_index(syscall)
#        print('%s -> %d'% (syscall, index))
        self.bosc[index] += 1
#        print(self.bosc)
        self.syscalls += 1
        if self.isTraining:
            self.add_bosc(self.bosc)
        else:
            if not self.is_normal_bosc(self.bosc):
                self.mismatches += 1
                if not self.is_already_declared and self.syscalls < self.epoch_size and self.mismatches >= self.detection_threshold:
                    print("Anomaly signal raised")
                    self.is_already_declared = True
                    if self.isUnderAttack:
                        self.rates.increment_tp()
                    else:
                        self.rates.increment_fp()
        
        # If end of epoch, start new epoch
        if self.syscalls >= self.epoch_size:
            self.syscalls = 0
            self.total_epochs += 1
            if self.isTraining:
                print("Epoch %d: database size: %d" % (self.total_epochs, len(self.db)))
                self.training_epochs += 1
            else:
                print("Epoch %d: %d mismatches" % (self.total_epochs, self.mismatches))
                self.mismatches = 0
                self.is_already_declared = False

    def dump_db_to_file(self):
        file = 'db-' + str(self.epoch_size)
        print('Dumping database to %s' % file)
        with open(file, 'w') as f:
            for bosc in self.db:
                f.write('%s\n' % bosc)


if __name__ == '__main__':
    rhids(sys.argv[1], sys.argv[2], sys.argv[3])
