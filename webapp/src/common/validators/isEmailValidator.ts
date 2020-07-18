const isEmailValidator = (rule: any, value: string, callback: Function) => {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(value)) {
        callback();
    } else {
        callback(new Error('Введите настоящий адрес электронной почты'));
    }
}

export default isEmailValidator;