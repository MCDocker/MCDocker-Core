package auth

import (
	"fmt"
	"mcdocker/internal/auth"

	"github.com/spf13/cobra"
)

var AuthCmd = &cobra.Command{
	Use:   "auth",
	Short: "Authentication commands",
	Run: func(cmd *cobra.Command, args []string) {
		cmd.Help()
	},
}

var loginCmd = &cobra.Command{
	Use:   "login",
	Short: "Login to your Minecraft account",
	Run: func(cmd *cobra.Command, args []string) {
		account := auth.Login()
		if account == nil {
			return
		}
		fmt.Printf("Logged in as %s\n", account.Username)
	},
}

func init() {
	AuthCmd.AddCommand(loginCmd)
}
