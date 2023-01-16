import { sveltekit } from '@sveltejs/kit/vite';
import mockPlugin from 'vite-plugin-file-mock';

/** @type {import('vite').UserConfig} */
const config = {
	plugins: [
		sveltekit(),
		// mockPlugin()
	],
	server: {
		host: true,
		proxy: {
			'/api': 'http://localhost:7000'
		}
	}
};

export default config;
