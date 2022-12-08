import subprocess
import json
from multiprocessing import Process, Pool, Manager


# print('hello')
ida_list = []
eda_list = []
All_IA_pattern_list = []


def process(patternStr):
    # print("Pattern: ", patternStr)
    argStr = '--regex=' + patternStr
    output = subprocess.run(['C:\Program Files\\Git\\bin\\bash.exe', 'run.sh', argStr], stdout=subprocess.PIPE).stdout.decode('utf-8')
    try:
        

        edastring = 'Contains EDA'
        idastring = 'Contains IDA: true'

        print(output)
        pattern_dict = {}
        if output.find(edastring, 450) > 0:
            print("EDA found for pattern: ", patternStr)
            # eda_list.append(patternStr)
            pattern_dict['pattern'] = patternStr
            pattern_dict['SLtype'] = 'EDA'
            All_IA_pattern_list.append(pattern_dict)
        elif output.find(idastring, 450) > 0:
            print("IDA found for pattern: ", patternStr)
            pattern_dict['pattern'] = patternStr
            pattern_dict['SLtype'] = 'IDA'
            All_IA_pattern_list.append(pattern_dict)
            # ida_list.append(patternStr)
    except:
        print('Some parsing error')

if __name__ == '__main__':
    with open('All_Non_SL_11k.txt') as f:
        patternList = []
        with Manager() as manager:
            eda_list = manager.list()
            ida_list = manager.list()
            cur = 0
            for line in f:
                jsonObj = json.loads(line)
                # print("JSON: ", line)
                # patternStr = jsonObj['csharpPattern']
                # patternStr = patternStr.rstrip()
                # print("Pattern: ", patternStr)
                pattern = jsonObj['pattern']
                # print("Patterns: ", patterns)
                patternList.append(pattern)
            print(len(patternList), patternList[10])
            p = Pool(7)
            p.map(process, patternList)
            # print(eda_list)
            # print(ida_list)
            # print(eda_list)
        with open('All_IA_11k_NONSL.txt', 'w') as outfile:
            for aDict in All_IA_pattern_list:
                json.dump(aDict,outfile)
                outfile.write('\n')
