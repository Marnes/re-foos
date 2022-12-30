import _ from 'lodash';
import { AlphaKeyCodes } from '$src/models/key-codes';

const TEAM_ATTRIBUTE = 'data-team';

export const onScoreInput = (inputElements: any[], minScore: number, maxScore: number): Function => {
    return (node: any) => {
        const handleInput = (event: any) => {
            if (event.cancelable) { //Hack to prevent other value update triggering another update
                return;
            }

            const elementIndex = _.indexOf(inputElements, node);
            const teamNumber = node.getAttribute(TEAM_ATTRIBUTE);

            if (node.value > maxScore) {
                node.value = maxScore;
            }

            if (node.value < minScore) {
                node.value = minScore;
            }

            if (node.value * 10 >= maxScore) {
                setTimeout(() => { //If input is still disabled
                    jumpToNext(elementIndex, teamNumber);
                }, 100);
            }
        }

        const handleKeyPress = (event: KeyboardEvent) => {
            const elementIndex = _.indexOf(inputElements, node);
            const teamNumber = node.getAttribute(TEAM_ATTRIBUTE);

            if (event.key === AlphaKeyCodes.ENTER) {
                jumpToNext(elementIndex, teamNumber);
            }

            if (event.key === AlphaKeyCodes.TAB) {
                event.preventDefault();
                jumpToNext(elementIndex, teamNumber);
            }
        }

        const handleFocus = (event: FocusEvent) => {
            node.select();
        }

        const jumpToNext = (elementIndex: number, teamNumber: String) => {
            node.blur();

            if (node.value == maxScore && teamNumber == '2') {
                const previousElement = inputElements[elementIndex - 1];

                if (previousElement) {
                    if (previousElement.value === maxScore || _.isNil(previousElement.value)) {
                        previousElement.focus();
                        return;
                    }
                }
            }

            if (elementIndex === inputElements.length) {
                return;
            }

            for (let i = elementIndex + 1; i < inputElements.length; i++) {
                const nextElement = inputElements[i];

                if (_.isNil(nextElement.value) && !nextElement.hasAttribute('disabled')) {
                    nextElement.focus();
                    return;
                }
            }
        }

        node.addEventListener('input', handleInput);
        node.addEventListener('focus', handleFocus);
        node.addEventListener('keydown', handleKeyPress);

        return {
            destroy() {
                node.removeEventListener('input', handleInput);
                node.removeEventListener('keydown', handleKeyPress);
            }
        };
    }
}
