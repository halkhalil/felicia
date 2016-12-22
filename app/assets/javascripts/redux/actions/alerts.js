export function add(category, text) {
	return {
		type: 'add',
		category: category,
		text: text,
		time: Date.now() / 1000
	}
}

export function addSuccess(text) {
	return add('success', text)
}


export function addInfo(text) {
	return add('info', text)
}


export function addWarning(text) {
	return add('warning', text)
}


export function addDanger(text) {
	return add('danger', text)
}

export function clean(timeout) {
	return {
		type: 'clean',
		timeout: timeout
	}
}

export function processingOn() {
	return {
		type: 'processingOn'
	}
}

export function processingOff() {
	return {
		type: 'processingOff'
	}
}