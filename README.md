# OrgStructureAnalyzer

## How to Run the Project
This project is built using Java 21 and Maven.

1. Prerequisites
- Java 21 installed
- Maven 3.9+ installed
- employee.csv placed in the project root (same folder as pom.xml)

2. Build the project
```bash
mvn clean package
```

3. Run the analyzer
```bash
mvn -q compile exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="employee.csv"
```

4. Output

###### The program prints:
- Salary deviations for managers
- Reporting-line depth violations
- Cycle detection (if any)

5. Sample Output:

- **Sample-Given:** Sample from the prompt (Martin underpaid, no depth issue)
```csv
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

- **Expected console output**
```bash
=========================================================
Managers earning less than they should:

- 124 Martin Chekov: underpaid by 15000.0000 (avg subordinate salary: 50000.00)
=========================================================
Managers earning more than they should:

=========================================================
Employees with reporting lines that are too long:

=========================================================
```


- **Scenario 1:** “Happy” data (valid org, one overpaid CEO)
```csv
Id,firstName,lastName,salary,managerId
1,Joe,Doe,90000,
2,Alice,Smith,60000,1
3,Bob,Brown,40000,2
4,Carol,Jones,50000,2
5,Dave,White,52000,1
6,Eve,Green,40000,5
```
- **Expected console output**
```bash
=========================================================
Managers earning less than they should:

=========================================================
Managers earning more than they should:

- 1 Joe Doe: overpaid by 6000.0000 (avg subordinate salary: 56000.00)
=========================================================
Employees with reporting lines that are too long:

=========================================================
```

- **Scenario 2:** Business-rule failures (underpaid, overpaid, deep chain)
```csv
Id,firstName,lastName,salary,managerId
1,Joe,Doe,90000,
2,Alice,Smith,50000,1
3,Bob,Brown,40000,2
4,Carol,Jones,50000,2
5,Dave,White,70000,1
6,Eve,Green,40000,5
7,Mike,Level1,50000,1
8,Nina,Level2,50000,7
9,Oscar,Level3,50000,8
10,Pam,Level4,50000,9
11,Quinn,Level5,50000,10
12,Rick,TooDeep,40000,11
```

- **Expected console output**
```bash
=========================================================
Managers earning less than they should:

- 2 Alice Smith: underpaid by 4000.0000 (avg subordinate salary: 45000.00)
- 7 Mike Level1: underpaid by 10000.0000 (avg subordinate salary: 50000.00)
- 8 Nina Level2: underpaid by 10000.0000 (avg subordinate salary: 50000.00)
- 9 Oscar Level3: underpaid by 10000.0000 (avg subordinate salary: 50000.00)
- 10 Pam Level4: underpaid by 10000.0000 (avg subordinate salary: 50000.00)
=========================================================
Managers earning more than they should:

- 1 Joe Doe: overpaid by 4999.9950 (avg subordinate salary: 56666.67)
- 5 Dave White: overpaid by 10000.0000 (avg subordinate salary: 40000.00)
=========================================================
Employees with reporting lines that are too long:

- 12 Rick TooDeep: 1 levels too deep (has 5 managers between them and the CEO)
=========================================================
```

- **Scenario 3:** Structural failure – missing manager
```csv
Id,firstName,lastName,salary,managerId
1,Joe,Doe,90000,
2,Sam,Orphan,40000,99
```

- **Expected console output**
```bash
Error while parsing CSV file: Manager with id 99 not found for employee 2
```

- **Scenario 4:** Structural failure – multiple CEOs
```csv
Id,firstName,lastName,salary,managerId
1,Joe,Doe,90000,
2,Jane,Roe,95000,
3,Bob,Brown,40000,1
4,Carol,Jones,45000,2
```

- **Expected console output**
```bash
Error while parsing CSV file: Multiple CEOs found
```
