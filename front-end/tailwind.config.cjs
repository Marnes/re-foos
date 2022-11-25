const config = {
    content: [
        './src/**/*.{html,js,svelte,ts}',
        './node_modules/@brainandbones/skeleton/**/*.{html,js,svelte,ts}'
    ],

    theme: {
        extend: {
            colors: {
                'gold': {
                    200: '#FCC203',
                    300: '#E9B302',
                    400: '#D6A402',
                    500: '#C49501',
                    600: '#B18601',
                    700: '#9E7700',
                },
                'silver': {
                    200: '#D8D8D6',
                    300: '#C3C3C1',
                    400: '#AFAEAD',
                    500: '#A4A4A6',
                    600: '#828282',
                    700: '#737373',
                },
                'bronze': {
                    200: '#CD7F31',
                    300: '#B56E26',
                    400: '#9E5D1C',
                    500: '#864B11',
                    600: '#6E3A06',
                    700: '#391506',
                },
                'positive': {
                    DEFAULT: '#16A34A',
                },
                'negative': {
                    DEFAULT: '#DC2626'
                }
            },
        }
    },
    plugins: [
        require('@brainandbones/skeleton/tailwind/theme.cjs'),
        require('@tailwindcss/forms'),
    ],
    darkMode: 'class',
};

module.exports = config;
