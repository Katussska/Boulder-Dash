const fs = require('fs');
const {DOMParser, XMLSerializer} = require('xmldom');

fs.readFile('01.tmx', 'utf-8', (err, d) => {
    if (err) {
        console.error('Error reading the file:', err);
        return;
    }

    const parser = new DOMParser();
    const xml = parser.parseFromString(d, 'text/xml');

    let objs = xml.getElementsByTagName('object');

    Array.from(objs).forEach((o) => {
        let id = o.getAttribute('gid');
        switch (id) {
            case '1':
                o.setAttribute('type', 'brick')
                break
            case '2':
                o.setAttribute('type', 'dirt')
                break
            case '4':
                o.setAttribute('type', 'stone')
                break
            case '6':
                o.setAttribute('type', 'portal')
                break
            case '7':
                o.setAttribute('type', 'star')
                break
        }
    })

    const s = new XMLSerializer();
    const xmlS = s.serializeToString(xml);

    fs.writeFile('01.tmx', xmlS, 'utf-8', () => {
    });
})
