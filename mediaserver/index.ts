import * as path from "path";
import  {Request, Response} from "express";

const cors = require("cors");
const multer = require("multer");
const express = require("express");
const fs = require('fs');

const app = express();
const corsOptions = {
    origin: '*',
};
const upload = multer({dest: 'files/'});
app.use(express.urlencoded({extended: true}))
app.use(cors(corsOptions));

app.get('/uploads/:id/:filename', (req, res) => {
    res.sendFile(path.resolve(__dirname  + `/../uploads/${req.params.id}/${req.params.filename}`));
})

app.post('/upload/:id',  upload.single('attachment'),  (req: Request, res: Response) => {
    const userId = req.params.id;

    const filePath = path.join(__dirname, `../${req.file.path}`);
    const originalFileName = req.file.originalname;
    const destination = path.join(__dirname, `../uploads/${userId}/${originalFileName}`)
    const userDirectory = path.join(__dirname, `../uploads/${userId}`);

    if (!fs.existsSync(userDirectory)) {
        fs.mkdirSync(userDirectory);
    }

    fs.copyFile(filePath, destination, (err) => {
        if (err) {
            console.log(err)
            res.sendStatus(400);
            res.send('Error')
        }
        res.send(`/uploads/${userId}/${originalFileName}`);
    });
});

app.listen(3090, () => console.log("Example app listening on port 3090!"));