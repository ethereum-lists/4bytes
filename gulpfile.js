const fs = require('fs');
const path = require('path');
const got = require('got');
const gulp = require('gulp');

gulp.task('download-signatures', (cb) => {
    got('https://www.4byte.directory/api/v1/signatures/?page_size=20000&ordering=created_at', {
        json: true
    })
    .then((res) => {
        const signatures = {};
        if (res.statusCode !== 200) {
            throw new Error(res.statusText);
        }
        res.body.results.forEach(e => signatures[e.hex_signature] = e.text_signature);
        fs.writeFileSync(path.join('dist', 'signatures.json'), JSON.stringify(signatures, null, 4));
        cb();
    })
    .catch(cb);
});

