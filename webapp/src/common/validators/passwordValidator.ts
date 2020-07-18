const validatePass = (rule: any, value: string, callback: Function) => {
    if (!value || value.length < 6) {
        callback(new Error('Введите пароль длинее 6 знаков'));
    } else {
        callback();
    }
};

export default validatePass;