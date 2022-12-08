import subprocess
import json

All_SL_pattern_list = []
All_NonSL_pattern_list = []
All_SL_patterns = []
with open('SL-regexes-for-chida.json') as f:
    for line in f:
        jsonObj = json.loads(line)
        pattern = jsonObj['pattern']
        All_SL_patterns.append(pattern)

with open('supported_regexes.txt') as f:
    patternList = []    
    cur = 0
    lineno = 0 
    for line in f:
        jsonObj = json.loads(line)
        print("line no: ", lineno+1)
        lineno = lineno + 1
        # print("JSON: ", line)
        # patternStr = jsonObj['csharpPattern']
        # patternStr = patternStr.rstrip()
        # print("Pattern: ", patternStr)
        pattern = jsonObj['pattern']
        if pattern in All_SL_patterns:
            if not pattern.startswith("^"):
                pattern = "^.*?" + pattern
            aDict = {"pattern": pattern}
            All_SL_pattern_list.append(aDict)
        else:
            aDict = {"pattern": pattern}
            All_NonSL_pattern_list.append(aDict)
    print(len(All_SL_pattern_list), All_SL_pattern_list[0])
        
        # print(eda_list)
        # print(ida_list)
        # print(eda_list)
with open('All_SL_220k.txt', 'w') as outfile:
    for aDict in All_SL_pattern_list:
        json.dump(aDict,outfile)
        outfile.write('\n')
with open('All_Non_SL_220k.txt', 'w') as outfile:
    for aDict in All_NonSL_pattern_list:
        json.dump(aDict,outfile)
        outfile.write('\n')
