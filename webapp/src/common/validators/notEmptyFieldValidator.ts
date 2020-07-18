const notEmptyFieldValidator = (rule: any, value: string, callback: Function) => {
    if (value === '') {
        callback(new Error('Поле не должно быть пустым'));
    } else {
        callback();
    }
};

export default notEmptyFieldValidator;