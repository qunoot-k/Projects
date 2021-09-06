
html_doc = """<html><head><title>The Dormouse's story</title></head>
<body>
<p class="title"><b>The Dormouse's story</b></p>

<p class="story">Once upon a time there were three little sisters; and their names were
<a href="http://example.com/elsie" class="sister" id="link1">Elsie</a>,
<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>

<p class="story">...</p>
"""

from bs4 import BeautifulSoup
soup = BeautifulSoup(html_doc, 'html.parser')
print(soup)

# use find_all to find all occurances of text in 'a' tags
sisters = soup.find_all('a')
for sis in sisters:
  print(sis.get_text())

# replace all http with https in a tags
for a in soup.findAll('a'):
  a['href'] = a['href'].replace("http://", "https://")
# print new html
print(soup)
