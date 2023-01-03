import _ from 'lodash';
import { AlphaKeyCodes } from '$src/models/key-codes';

const TEAM_ATTRIBUTE = 'data-team';

export const onScoreInput = (inputElements: any[], minScore: number, maxScore: number): Function => {
    return (node: any) => {
        const handleInput = (event: any) => {
            let value = Number(node.value)
            const elementIndex = _.indexOf(inputElements, node);
            const teamNumber = node.getAttribute(TEAM_ATTRIBUTE);

            if (value > maxScore) {
                node.value = maxScore;
            }

            if (value < minScore) {
                node.value = minScore;
            }

            value = Number(node.value)

            if (value * 10 >= maxScore || value === 0) {
                setTimeout(() => { //If input is still disabled
                    jumpToNext(value, elementIndex, teamNumber);
                }, 50);
            }
        }

        const handleKeyPress = (event: KeyboardEvent) => {
            const elementIndex = _.indexOf(inputElements, node);
            const teamNumber = node.getAttribute(TEAM_ATTRIBUTE);

            if (event.key === AlphaKeyCodes.ENTER) {
                jumpToNext(Number(node.value), elementIndex, teamNumber);
            }

            if (event.key === AlphaKeyCodes.TAB) {
                event.preventDefault();
                jumpToNext(Number(node.value), elementIndex, teamNumber);
            }
        }

        const handleFocus = (event: FocusEvent) => {
            node.select();
        }

        const jumpToNext = (value: number, elementIndex: number, teamNumber: String) => {
            node.blur();

            if (value == maxScore && teamNumber == '2') {
                //Jump to left score if new right score is max and left score is max or null
                const previousElement = inputElements[elementIndex - 1];

                if (previousElement && (previousElement.value == maxScore || _.isEmpty(previousElement.value))) {
                    previousElement.focus();
                    return;
                }
            } else if (value == maxScore && teamNumber == '1') {
                //Force jump to next element if new left score is max and right is was max or null
                const nextElement = inputElements[elementIndex + 1];

                if (nextElement && (nextElement.value == maxScore || _.isEmpty(nextElement.value))) {
                    nextElement.focus();
                    return;
                }
            }

            if (elementIndex === inputElements.length) {
                return;
            }

            let jump = 1;

            if (value != maxScore && teamNumber == '1') {
                // Jump 2 if entering left score and not max amount, as right score will be autoed to max
                jump = 2;
            }


            for (let i = elementIndex + jump; i < inputElements.length; i++) {
                const nextElement = inputElements[i];

                if (_.isEmpty(nextElement.value) && !nextElement.hasAttribute('disabled')) {
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
                node.removeEventListener('focus', handleFocus);
                node.removeEventListener('keydown', handleKeyPress);
            }
        };
    }
}
