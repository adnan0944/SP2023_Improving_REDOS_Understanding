import subprocess
import json

All_SL_pattern_list = []
with open('project_patterns.txt') as f:
    patternList = []    
    cur = 0
    for line in f:
        jsonObj = json.loads(line)
        # print("JSON: ", line)
        # patternStr = jsonObj['csharpPattern']
        # patternStr = patternStr.rstrip()
        # print("Pattern: ", patternStr)
        patterns = jsonObj['patterns']
        # print("Patterns: ", patterns)
        for pattern in patterns:
            argStr = '--regex=' + pattern
            output = subprocess.run(['./run.sh', argStr], stdout=subprocess.PIPE).stdout.decode('utf-8')
            # print(output)
            edastring = 'Contains EDA'
            idastring = 'Contains IDA'

    # print(output)
            pattern_dict = {}
            if output.find(edastring, 450) > 0:
                print(cur," EDA found for pattern: ", pattern)
                # eda_list.append(patternStr)
                pattern_dict['pattern'] = pattern
                pattern_dict['SLtype'] = 'EDA'
                pattern_dict['name'] = jsonObj['name']
                pattern_dict['owner'] = jsonObj['owner']
                All_SL_pattern_list.append(pattern_dict)
                cur += 1
            elif output.find(idastring, 450) > 0:
                print(cur, " IDA found for pattern: ", pattern)
                pattern_dict['pattern'] = pattern
                pattern_dict['SLtype'] = 'IDA'
                pattern_dict['name'] = jsonObj['name']
                pattern_dict['owner'] = jsonObj['owner']
                All_SL_pattern_list.append(pattern_dict)
                cur += 1
        if cur >= 100:
            break

    print(len(All_SL_pattern_list), All_SL_pattern_list[0])
        
        # print(eda_list)
        # print(ida_list)
        # print(eda_list)
with open('All_SL_Maven.txt', 'w') as outfile:
    for aDict in All_SL_pattern_list:
        json.dump(aDict,outfile)
        outfile.write('\n')
