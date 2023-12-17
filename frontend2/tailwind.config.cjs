/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{svelte,js,ts}'],
  theme: {
    fontFamily: {
        sans: "Verdana, Geneva, Tahoma, sans-serif"
    }
  },
  plugins: [require('daisyui')],
  daisyui: {
    themes: [
      {
        light: {
          ...require("daisyui/src/theming/themes")["bumblebee"],
          "base-100": "#f6f8ff",
          "base-200": "#e8ebf9",
          "base-content": "#302f33"
        }
      },
      {
        dark: {
          ...require("daisyui/src/theming/themes")["black"],
          "base-100": "#302f33",
          "base-200": "#2a282e",
          "base-content": "#f6f8ff",
          "--rounded-box": "1rem",
          "--rounded-btn": "0.5rem",
          "--rounded-badge": "1.9rem",
          "--animation-btn": "0.25s",
          "--animation-input": ".2s",
          "--btn-focus-scale": "0.95",
          "--border-btn": "1px",
          "--tab-border": "1px",
          "--tab-radius": "0.5rem",
        }
      }
    ],
    darkTheme: "dark"
  }
};

