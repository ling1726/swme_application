# -*- coding: utf-8 -*-

## the csv was generated with the command
# \copy (select person_pk, Address from person order by person_pk) to '~/addresses.csv' (DELIMITER '|')

# the first token with digits must be street number
def getStreetNumber(tokens):
    for token in tokens:
        if any(char.isdigit() for char in token): return token

# all the string tokens that do not contain digits and are before PLZ identifier
def getStreet(tokens):
    street = ''
    for token in tokens:
        if token == 'Address:': continue
        if token == 'PLZ:': return street
        if not any(char.isdigit() for char in token): street += ' '+token
    return street

# token immediately after the PLZ identifier
def getPostCode(tokens):
    pos = 0
    for token in tokens:
        if token == 'PLZ:': break
        pos += 1
    return tokens[pos+1]

# all tokens after the City identifier
def getCity(tokens):
    pos = 0
    city = ''
    foundCity = False
    for token in tokens:
        if foundCity: city += ' '+token
        if token == 'City:': foundCity = True 
    return city 
            



# contains all address objects
addresses = []

with open('addresses.csv', 'rw') as inFile:
    for line in inFile:
        tokens = line.strip('\n').split('|')
        address = {}
        address['personId'] = int(tokens[0])
        tokens = tokens[1].split()
        address['street'] = getStreet(tokens)
        address['streetNumber'] = getStreetNumber(tokens)
        address['postCode'] = getPostCode(tokens)
        address['city'] = getCity(tokens)
        addresses.append(address)

#for i in range(0,len(addresses)): print addresses[i]

##### add logic to check for country if time permits ######
#Webservice Type : REST /JSON 
#Url : api.geonames.org/postalCodeLookupJSON?
#Parameters : postalcode,country ,maxRows (default = 20),callback, charset (default = UTF-8)
#Result : returns a list of places for the given postalcode in JSON format, sorted by postalcode,placename 
#Example http://api.geonames.org/postalCodeLookupJSON?postalcode=6600&country=AT&username=demo
########################################################### 

#### generate sql script ####
addressInserts = []
personUpdates = []

for address in addresses:
    insert = "INSERT INTO ADDRESS VALUES (nextval('SQ_ADDRESS_PK'), "
    insert += "'"+address['street'] + "', "
    insert += "'"+address['streetNumber'] + "', "
    insert += "'"+address['postCode'] + "', "
    insert += "'"+address['city'] + "', "
    insert += "null, null);"
    addressInserts.append(insert)

    update = "UPDATE PERSON SET ADDRESS_ID = currval('SQ_ADDRESS_PK') WHERE PERSON_PK = "+str(address['personId'])+";"
    personUpdates.append(update)

#for insert in addressInserts: print insert  
#for update in personUpdates: print update

with open('tmp.sql', 'w') as outFile:
    for i in range(0, len(addresses)):
        outFile.write(addressInserts[i]+'\n')
        outFile.write(personUpdates[i]+'\n')
    


