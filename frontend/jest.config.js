module.exports = {
    preset: 'ts-jest',
    testEnvironment: 'node',
    collectCoverageFrom: [
        '!**/node_modules/**',
        '!**/vendor/**',
        '!<rootDir>/src/App.tsx',
        '!src/index.tsx',
        '!src/reportWebVitals.ts',
        "!<rootDir>/src/types/*.{ts,tsx}",
        "<rootDir>/src/types/*.{ts,tsx}"

    ],
    coverageDirectory: './coverage',
    coverage: true,
    collectCoverage: true,
    testPathIgnorePatterns: [
        "<rootDir>/src/types/*.{ts,tsx}"
    ]
};