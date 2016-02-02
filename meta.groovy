package util
@Grab('org.hsqldb:hsqldb:2.3.2')
@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql

def url = 'jdbc:hsqldb:mem:GinA'
def user = 'sa'
def password = ''
def driver = 'org.hsqldb.jdbcDriver'
def sql = Sql.newInstance(url, user, password, driver)

sql.execute '''
    CREATE TABLE Athlete (
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    dateOfBirth DATE
    );
'''

sql.execute '''
    INSERT INTO Athlete (firstname, lastname, dateOfBirth)
    VALUES ('Paul', 'Tergat', '1969-06-17')
'''

def data = [first: 'Khalid', last: 'Khannouchi', birth: '1971-12-22']
def keys = sql.executeInsert """
    INSERT INTO Athlete (firstname, lastname, dateOfBirth)
    VALUES (${data.first}, ${data.last}, ${data.birth})
"""
println ' Row Count '.center(25,'-')
println sql.firstRow('SELECT COUNT(*) as num FROM Athlete').num ;

println ' Athlete Info '.center(25,'-')

def fmt = new java.text.SimpleDateFormat('dd. MMM yyyy (E)',
        Locale.US)

sql.eachRow('SELECT * FROM Athlete'){ athlete ->
    println athlete.firstname + ' ' + athlete.lastname
    println 'born on '+ fmt.format(athlete.dateOfBirth)
    println '-' * 25
}

println(keys[0]);
assert keys[0] == [1]


def insertSql = '''
    INSERT INTO Athlete (firstname, lastname, dateOfBirth)
    VALUES (?,?,?)
    '''
def params = ['Ronaldo', 'da Costa', '1970-06-07']
def keyColumnNames = ['ATHLETEID']
  keys = sql.executeInsert insertSql, params, keyColumnNames
println(keys[0]);
assert keys[0] == [ATHLETEID: 2]

def qry = '''
INSERT INTO Athlete (firstname, lastname, dateOfBirth)
VALUES (?,?,?)
'''
sql.withBatch(3, qry) { ps ->
    ps.addBatch('Paula', 'Radcliffe', '1973-12-17')
    ps.addBatch('Catherine', 'Ndereba', '1972-07-21')
    ps.addBatch('Naoko', 'Takahashi', '1972-05-06')
    ps.addBatch('Tegla', 'Loroupe', '1973-05-09')
    ps.addBatch('Ingrid', 'Kristiansen', '1956-03-21')
}

println ' Row Count '.center(25,'-')
println sql.firstRow('SELECT COUNT(*) as num FROM Athlete').num ;

sql.execute '''
DROP TABLE Athlete IF EXISTS;
'''
// use 'sql' instance ...
sql.close()
