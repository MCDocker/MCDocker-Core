package auth

import (
	"bufio"
	"encoding/json"
	"fmt"
	"mcdocker/internal/utils"
	"os"
	"strings"
)

type Account struct {
	Username    string `json:"username"`
	UUID        string `json:"uuid"`
	AccessToken string `json:"accessToken"`
}

type errorCodes struct {
	Err *int
	Msg *string
}

func New(username string, uuid string, accessToken string) *Account {
	return &Account{
		Username:    username,
		UUID:        uuid,
		AccessToken: accessToken,
	}
}

func Login() *Account {
	code, msErr := authMicrosoft()
	if msErr != nil {
		fmt.Println("Error logging in with Microsoft account:", *msErr)
		return nil
	}

	xblToken, userhash, xblError := authXBL(*code)
	if xblError != nil {
		fmt.Println("Error logging in with XBL account: ", xblError.Error())
		return nil
	}

	xstsToken, xstsErr := authXSTS(*xblToken)
	if xstsErr != nil {
		fmt.Println("Error logging in with XSTS account: ", *xstsErr.Msg)
		return nil
	}

	accessToken, mcErr := authMinecraft(*userhash, *xstsToken)
	if mcErr != nil {
		fmt.Println("Error logging in with Minecraft account: ", mcErr.Error())
		return nil
	}

	account, profileErr := getProfile(*accessToken)
	if profileErr != nil {
		fmt.Println("Error getting profile: ", profileErr.Error())
		return nil
	}

	return account
}

func getProfile(mcToken string) (account *Account, err error) {
	res := utils.Get("https://api.minecraftservices.com/minecraft/profile", map[string]interface{}{
		"Authorization": "Bearer " + mcToken,
	})

	if res.Err != nil {
		return nil, res.Err
	}

	data := struct {
		Name string `json:"name"`
		UUID string `json:"id"`
	}{}
	json.Unmarshal([]byte(res.Body), &data)
	return New(data.Name, data.UUID, mcToken), nil
}

func checkOwnership(mcToken string) (err error) {
	res := utils.Get("https://api.minecraftservices.com/entitlements/mcstore", map[string]interface{}{
		"Authorization": "Bearer " + mcToken,
	})

	if res.Err != nil {
		return res.Err
	}

	data := struct {
		Items []struct {
			Name string `json:"name"`
		} `json:"items"`
	}{}
	json.Unmarshal([]byte(res.Body), &data)

	for _, item := range data.Items {
		if item.Name == "game_minecraft" || item.Name == "product_minecraft" {
			return nil
		}
	}

	return fmt.Errorf("Account does not own Minecraft")
}

func authMinecraft(userhash string, xstsToken string) (accessToken *string, err error) {
	body, _ := json.Marshal(map[string]interface{}{
		"identityToken": "XBL3.0 x=" + userhash + ";" + xstsToken,
	})

	res := utils.Post("https://api.minecraftservices.com/authentication/login_with_xbox", map[string]interface{}{
		"Content-Type": "application/json",
	}, string(body))

	if res.Err != nil {
		return nil, res.Err
	}

	data := struct {
		AccessToken string `json:"access_token"`
	}{}
	json.Unmarshal([]byte(res.Body), &data)
	return &data.AccessToken, nil
}

func authMicrosoft() (authToken *string, retErr *error) {
	err := utils.Open("https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf")

	if err != nil {
		return nil, &err
	}

	fmt.Println("Copy the code from the URL after you have logged in")
	var code string
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		if !strings.HasPrefix(line, "M.R3") {
			fmt.Println("Invalid code!")
			continue
		}
		code = strings.Split(line, "&")[0]
		fmt.Println("Starting login process")
		break
	}
	return &code, nil
}

func authXSTS(token string) (xstsToken *string, err *errorCodes) {
	body, _ := json.Marshal(map[string]interface{}{
		"Properties": map[string]interface{}{
			"SandboxId": "RETAIL",
			"UserTokens": []string{
				token,
			},
		},
		"RelyingParty": "rp://api.minecraftservices.com/",
		"TokenType":    "JWT",
	})

	res := utils.Post("https://xsts.auth.xboxlive.com/xsts/authorize", map[string]interface{}{
		"Content-Type": "application/json",
	}, string(body))

	if res.Err != nil {
		data := struct {
			Message *string
			XErr    *int
		}{}
		json.Unmarshal([]byte(res.Body), &data)
		return nil, &errorCodes{
			Err: data.XErr,
			Msg: data.Message,
		}
	}

	data := struct {
		Token string `json:"Token"`
	}{}
	json.Unmarshal([]byte(res.Body), &data)

	return &data.Token, nil
}

func authXBL(code string) (xblToken *string, userhash *string, err error) {
	body, _ := json.Marshal(map[string]interface{}{
		"RelyingParty": "http://auth.xboxlive.com",
		"TokenType":    "JWT",
		"Properties": map[string]interface{}{
			"AuthMethod": "RPS",
			"SiteName":   "user.auth.xboxlive.com",
			"RpsTicket":  "d=" + code,
		},
	})

	// WHY DOES THIS RETURN STATUS 400

	res := utils.Post("https://user.auth.xboxlive.com/user/authenticate", map[string]interface{}{
		"Content-Type": "application/json",
		"Accept":       "application/json",
	}, string(body))

	fmt.Println(res.Res.Status)

	if res.Err != nil {
		return nil, nil, res.Err
	}

	fmt.Println(res.Body)

	data := struct {
		Token         string `json:"Token"`
		DisplayClaims struct {
			XUI []struct {
				UHS string `json:"uhs"`
			} `json:"xui"`
		} `json:"DisplayClaims"`
	}{}

	fmt.Println(data)

	json.Unmarshal([]byte(res.Body), &data)

	fmt.Println(data)

	return &data.Token, &data.DisplayClaims.XUI[0].UHS, nil
}
