const isPhoneValidator = (rule: any, value: string, callback: Function) => {
    if (!value) {
        callback();
    } else if (/^(\s*)?(\+)?([- _():=+]?\d[- _():=+]?){10,14}(\s*)?$/.test(value)) {
        callback();
    } else {
        callback(new Error('Введите настоящий телефон'));
    }
}

export default isPhoneValidator;