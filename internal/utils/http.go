package utils

import (
	"bytes"
	"crypto/tls"
	"fmt"
	"io"
	"mcdocker/internal"
	"net/http"
	"net/http/httputil"
)

type Response struct {
	Body string
	Res  *http.Response
	Err  error
}

func Post(url string, headers map[string]interface{}, body string) Response {
	client := http.Client{}
	client.Transport = &http.Transport{
		TLSClientConfig: &tls.Config{
			Renegotiation:      tls.RenegotiateOnceAsClient,
			InsecureSkipVerify: true,
		},
	}
	req, _ := http.NewRequest("POST", url, bytes.NewBufferString(body))
	for key, value := range headers {
		req.Header.Set(key, value.(string))
	}
	req.Header.Set("User-Agent", internal.Name+"/"+internal.Version)
	req.Header.Set("Content-Type", "application/json")

	fmt.Printf("Request: %s", req.Body)

	dump, _ := httputil.DumpRequestOut(req, true)

	fmt.Println(string(dump))

	res, resErr := client.Do(req)

	if resErr != nil {
		defer res.Body.Close()
		resBody, _ := io.ReadAll(res.Body)

		return Response{
			Body: string(resBody),
			Err:  resErr,
		}
	}

	defer res.Body.Close()
	resBody, _ := io.ReadAll(res.Body)

	resDump, _ := httputil.DumpResponse(res, true)
	fmt.Println(string(resDump))

	return Response{
		Body: string(resBody),
		Res:  res,
	}
}

func Get(url string, headers map[string]interface{}) Response {
	client := http.Client{}
	client.Transport = &http.Transport{
		TLSClientConfig: &tls.Config{
			Renegotiation:      tls.RenegotiateOnceAsClient,
			InsecureSkipVerify: true,
		},
	}
	req, _ := http.NewRequest("GET", url, nil)
	for key, value := range headers {
		req.Header.Set(key, value.(string))
	}
	req.Header.Set("User-Agent", internal.Name+"/"+internal.Version)
	res, resErr := client.Do(req)

	if resErr != nil {
		return Response{
			Err: resErr,
		}
	}

	defer res.Body.Close()
	resBody, _ := io.ReadAll(res.Body)

	return Response{
		Body: string(resBody),
		Res:  res,
	}
}
