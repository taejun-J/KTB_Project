import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 1,
    iterations: 300,
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const type = __ENV.TYPE;
    const keyword = __ENV.KEYWORD;

    const url =
        `${BASE_URL}/posts/search/benchmark/${type}?keyword=${keyword}`;

    const res = http.get(url);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
